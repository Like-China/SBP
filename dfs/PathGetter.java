package dfs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Get all possible paths from a vertex to another using DFS
 * with a simple pruning strategy
 */
public class PathGetter {
	/**
	 * The stack that temporarily holds the nodes of a path
	 */
	public static Stack<Node> stack = new Stack<Node>();
	public static ArrayList<Object[]> sers = new ArrayList<Object[]>();
 
	/**
	 * determinate if the node is in the stack
	 * @param node a node
	 * @return  true(in stack) false(not in stack)
	 */
	public static boolean isNodeInStack(Node node)
	{
		Iterator<Node> it = stack.iterator();
		while (it.hasNext()) {
			Node node1 = (Node) it.next();
			if (node == node1)
				return true;
		}
		return false;
	}

	public static void showAndSavePath()
	{
		Object[] o = stack.toArray();
		sers.add(o); 
	}
 
	/*
	 * get paths
	 * cNode: current Node
	 * pNode: previous Node
	 * sNode: start Node
	 * eNode: end Node
	 */
	public static boolean getPaths(Node cNode, Node pNode, Node sNode, Node eNode) {
		Node nNode = null;
		
		if (cNode != null && pNode != null && cNode == pNode)
			return false;
		
		
		if (cNode != null) {
			int i = 0;
			/* add the start vertex into stack */
			stack.push(cNode);
			if (cNode == eNode)
			{
				showAndSavePath();
				return true;
			}
			else
			{
				nNode = cNode.getRelationNodes().get(i);
				while (nNode != null) {
					if (pNode != null
							&& (nNode == sNode || nNode == pNode || isNodeInStack(nNode))) {
						i++;
						if (i >= cNode.getRelationNodes().size())
							nNode = null;
						else
							nNode = cNode.getRelationNodes().get(i);
						continue;
					}
					if (getPaths(nNode, cNode, sNode, eNode))
					{
						/**
						 * if a route is found, pop the top element of the stack
						 */
						stack.pop();
					}
					i++;
					if (i >= cNode.getRelationNodes().size())
						nNode = null;
					else
						nNode = cNode.getRelationNodes().get(i);
				}
				stack.pop();
				return false;
			}
		} else
			return false;
	}

}