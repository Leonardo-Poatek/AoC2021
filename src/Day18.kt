import java.io.File
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.test.assertEquals

private val realInput = File("inputs/Day18Input.txt").readLines()

private class TreeNode(var parent: TreeNode?) {

    var left: TreeNode? = null
        set(value) {
            field = value?.also { it.parent = this }
        }

    var right: TreeNode? = null
        set(value) {
            field = value?.also { it.parent = this }
        }

    var value: Int? = null

    val magnitude: Long
        get() {
            return value?.toLong() ?: 3 * left!!.magnitude + 2 * right!!.magnitude
        }

    val height: Int
        get() = parent?.height?.plus(1) ?: 1

    constructor(parent: TreeNode?, value: Int) : this(parent) {
        this.value = value
    }

    constructor(parent: TreeNode?, left: TreeNode?, right: TreeNode?) : this(parent) {
        this.left = left
        this.right = right
    }

    fun updateChildParents() {
        left?.parent = this
        right?.parent = this
    }

    override fun toString(): String {
        return value?.toString() ?: "[$left,$right]"
    }

    companion object {

        fun from(input: String): TreeNode {
            var i = 0

            fun innerParse(input: String, parent: TreeNode?): TreeNode {

                if (input[i].isDigit()) return TreeNode(parent, Character.getNumericValue(input[i++]))

                val root = TreeNode(parent)

                i++ //Skips [
                root.left = innerParse(input, root)

                i++ //Skips ,
                root.right = innerParse(input, root)

                i++ //Skips ]

                return root
            }

            return innerParse(input, null)
        }
    }
}

private class Tree(initialValue: String? = null) {

    var root: TreeNode? = initialValue?.let { TreeNode.from(it) }

    private val toReduce: Queue<TreeNode> = ArrayDeque()

    //Adds to the roots right
    fun add(node: TreeNode) {
        if (root != null) {
            root.let { root = TreeNode(null, it, node) }
        } else {
            root = node
        }

        root!!.updateChildParents()

        //Check for reduce criterea
        //If you perform a single split, you gotta do the explode loop again.

        var didSplit = true
        var didExplode = true

        while (didSplit) {
            didExplode = true
            while (didExplode) {
                didExplode = checkForExplode(root!!)
            }
            didSplit = checkForSplit(root!!)
        }
    }

    fun getMagnitude() = root!!.magnitude

    fun getHeight(node: TreeNode): Int {
        return 1 + max(node.left?.let { getHeight(it) } ?: 0, node.right?.let { getHeight(it) } ?: 0)
    }

    private fun findClosestLeftNumber(node: TreeNode): TreeNode? {
        var current: TreeNode? = node.parent
        var prev: TreeNode? = node

        while (current != root && current?.left == prev) {
            current = current?.parent
            prev = prev?.parent
        }

        current = current?.left

        if (current == prev) return null

        //Now, go back to the right as far as possible
        while (current?.value == null) {
            current = current?.right
        }

        return current
    }

    private fun findClosestRightNumber(node: TreeNode): TreeNode? {
        var current: TreeNode? = node.parent
        var prev: TreeNode? = node

        while (current != root && current?.right == prev) {
            current = current?.parent
            prev = prev?.parent
        }

        current = current?.right

        if (current == prev) return null

        //Now, go back to the left as far as possible
        while (current?.value == null) {
            current = current?.left
        }

        return current
    }

    //Its only possible to explode pair nodes, not value nodes
    fun explode(node: TreeNode) {
        if (node.value != null) return
        if (node.left?.value == null) return
        if (node.right?.value == null) return

        //println("Exploding $node")

        val closestLeft = findClosestLeftNumber(node)
        val closestRight = findClosestRightNumber(node)
//        println(closestLeft)
//        println(closestRight)

        //Add the numbers
        closestLeft?.let { it.value = it.value!! + node.left!!.value!! }
        closestRight?.let { it.value = it.value!! + node.right!!.value!! }

        //Remove children
        node.left = null
        node.right = null
        node.value = 0

        //println(root.toString())
    }

    fun split(node: TreeNode) {
        node.value?.let { value ->
            //println("Splitting $node")
            node.left = TreeNode(node, floor(value / 2f).toInt())
            node.right = TreeNode(node, ceil(value / 2f).toInt())
            node.value = null
            //println(root.toString())
        }
    }

    /*
    During reduction, at most one action applies, after which the process returns to the
     top of the list of actions. For example, if split produces a pair that meets the
      explode criteria, that pair explodes before other splits occur.
     */

    fun checkForReduce(node: TreeNode): Boolean {
        if (node.value != null) {
            return if (node.height > 5) {
                explode(node.parent!!)
                true
            } else if (node.value!! > 9) {
                split(node)
                true
            } else {
                false
            }
        }

        return if (checkForReduce(node.left!!)) {
            true
        } else {
            checkForReduce(node.right!!)
        }
    }

    fun checkForExplode(node: TreeNode): Boolean {
        if (node.value != null) {
            return if (node.height > 5) {
                explode(node.parent!!)
                true
            } else {
                false
            }
        }

        return if (checkForExplode(node.left!!)) {
            true
        } else {
            checkForExplode(node.right!!)
        }
    }

    fun checkForSplit(node: TreeNode): Boolean {
        if (node.value != null) {
            return if (node.value!! > 9) {
                split(node)
                true
            } else {
                false
            }
        }

        return if (checkForSplit(node.left!!)) {
            true
        } else {
            checkForSplit(node.right!!)
        }
    }

}


class Day18(val input: List<String>) {

    private val operations = input.map { TreeNode.from(it) }

    fun solve1(): Long {
        val tree = Tree(input.first())
        operations.drop(1).forEach { tree.add(it) }
        return tree.getMagnitude()
    }

    fun solve2(): Long {
        var maxMag = 0L
        var maxA = ""
        var maxB = ""
        input.forEach { a ->
            input.forEach { b ->
                if (a != b) {
                    Tree(a).apply {
                        add(TreeNode.from(b))
                        getMagnitude().let {
                            if (it > maxMag) {
                                maxA = a
                                maxB = b
                                maxMag = it
                            }
                        }
                    }
                }
            }
        }
        return maxMag
    }

}

private fun test() {
    assertEquals("[1,2]", TreeNode.from("[1,2]").toString())
    assertEquals("[[1,9],[8,5]]", TreeNode.from("[[1,9],[8,5]]").toString())
    assertEquals("[[[1,2],[3,4]],[[5,6],[7,8]]]", TreeNode.from("[[[1,2],[3,4]],[[5,6],[7,8]]]").toString())
    assertEquals("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]", TreeNode.from("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]").toString())

    Tree("[[[[[9,8],1],2],3],4]").apply {
        explode(root?.left?.left?.left?.left!!)
        println(this.root)
    }

    Tree("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").apply {
        explode(root?.left?.right?.right?.right!!)
        println(this.root)
    }

    val tree = Tree("[[[[4,3],4],4],[7,[[8,4],9]]]")
    tree.add(TreeNode.from("[1,1]"))
    assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", tree.root.toString())

    val smallTest = listOf(
        "[1,1]",
        "[2,2]",
        "[3,3]",
        "[4,4]",
        "[5,5]",
        "[6,6]",
    )

    val tree3 = Tree("[1,1]")
    smallTest.forEach {
        tree3.add(TreeNode.from(it))
    }

    val bigTest = listOf(
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
        "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
        "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
        "[7,[5,[[3,8],[1,4]]]]",
        "[[2,[2,2]],[8,[8,1]]]",
        "[2,9]",
        "[1,[[[9,3],9],[[9,0],[0,7]]]]",
        "[[[5,[7,4]],7],1]",
        "[[[[4,2],2],6],[8,7]]",
    )

    val tree2 = Tree()
    bigTest.forEach {
        tree2.add(TreeNode.from(it))
    }
    assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", tree2.root.toString())

    val treeMag = Tree("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    assertEquals(3488, treeMag.getMagnitude())


    val magTest = listOf(
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
        "[[[5,[2,8]],4],[5,[[9,9],0]]]",
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
        "[[[[5,4],[7,7]],8],[[8,3],8]]",
        "[[9,3],[[9,9],[6,[4,9]]]]",
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
    )

    Day18(magTest).apply {
        assertEquals(3993, solve2())
    }
}


fun main() {
    test()
    Day18(realInput).apply {
        println(solve1().toString())
        println(solve2().toString())
    }
}
