package jabs.ledgerdata;

import jabs.network.node.nodes.Node;

import java.util.List;

public abstract class Block<B extends Block<B>> extends Data implements Comparable<Block<B>> {
    private final int height;
    private final double creationTime;
    private final List<B> parents;
    private final Node creator;

    protected Block(int size, int height, double creationTime, Node creator, List<B> parents, int hashSize) {
        super(size, hashSize);
        this.height = height;
        this.creationTime = creationTime;
        this.creator = creator;
        this.parents = parents;
    }

    public int getHeight() {
        return this.height;
    }

    public double getCreationTime() {
        return this.creationTime;
    }

    public Node getCreator() {
        return this.creator;
    }

    public List<B> getParents() {
        return this.parents;
    }

    public int compareTo(Block<B> b) {
        return Integer.compare(this.height, b.getHeight());
    }
}
