package algcmp1;

import java.util.LinkedHashMap;

public class Graph {
    private LinkedHashMap<Integer, Node> nodes;
    private int numOfNodes;
    private int numOfEdges;

    public static class Node {
        private int key;
        private int weight;
        private LinkedHashMap<Integer, Node> connections;
        private LinkedHashMap<Integer, Integer> connectionsWeights;

        public Node(int key, int weight) {
            this.key = key;
            connections = new LinkedHashMap<Integer, Node>();
            this.weight = weight;
            connectionsWeights = new LinkedHashMap<Integer, Integer>();
        }

        public void addConnection(Node node, int weight) {
            connections.put(node.key, node);
            connectionsWeights.put(node.key, weight);
        }

        public void removeConnection(Node node) {
            if (isConnected(node)) {
                connections.remove(node.key, node);
                connectionsWeights.remove(node.key, node);
            }
        }

        public int getKey() {
            return key;
        }

        public int getEdgeWeight(Node node) {
            return connectionsWeights.get(node.key);
        }

        public int getNodeWeight() {
            return weight;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public void setNodeWeight(int weight) {
            this.weight = weight;
        }

        public void setEdgeWeight(Node node, int weight) {
            if (!connectionsWeights.containsKey(node.key)) {
                connectionsWeights.put(node.key, weight);
            }
        }

        public boolean isConnected(Node node) {
            return connections.containsKey(node.key) && connections.get(node.key) == node;
        }
    }

    public Graph() {
        nodes = new LinkedHashMap<Integer, Node>();
        numOfNodes = 0;
        numOfEdges = 0;
    }

    public int getKey(Node node) {
        return node.getKey();
    }

    public int getWeight(Node node) {
        return node.getNodeWeight();
    }

    public void setKey(Node node, int key) {
        node.setKey(key);
    }

    public void setWeight(Node node, int weight) {
        node.setNodeWeight(weight);
    }

    public boolean hasNode(Node node) {
        return nodes.containsValue(node);
    }

    public boolean hasNode(int key) {
        return nodes.containsKey(key);
    }

    public Node getNode(int key) {
        if (hasNode(key)) {
            return nodes.get(key);
        }
        return null;
    }

    public void connect(int nodeNm1, int nodeNm2, int weight) {
        Node node1 = getNode(nodeNm1);
        Node node2 = getNode(nodeNm2);
        if (!node1.isConnected(node2)) {
            node1.addConnection(node2, weight);
        }
        if (!node2.isConnected(node1)) {
            node2.addConnection(node1, weight);
        }
        numOfEdges++;
    }

    public void addNode(Node node) {
        if (!hasNode(node)) {
            nodes.put(node.key, node);
            numOfNodes++;
        }
    }

    public void addNode(int key, int weight) {
        addNode(new Node(key, weight));
    }

    public void removeConnection(Node node1, Node node2) {
        if (!node1.isConnected(node2)) {
            node1.removeConnection(node2);
        }
        if (!node2.isConnected(node1)) {
            node2.removeConnection(node1);
        }
        numOfEdges--;
    }

    public void removeNode(Node node) {
        if (hasNode(node)) {
            nodes.remove(node.key, node);
            numOfNodes--;
        }
    }

    public boolean straightConnected(Node node1, Node node2) {
        return hasNode(node1) && hasNode(node2) && node1.isConnected(node2) && node2.isConnected(node1);
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public int getNumOfEdges() {
        return numOfEdges;
    }

    public LinkedHashMap<Integer,Node> getNodeMap(){
        return nodes;
    }

    public void johnson(){
        for(int i=0;i<numOfNodes;i++){
            dijkstra(getNode(i));
        }
    }

    public void dijkstra(Node node){
    }
}
