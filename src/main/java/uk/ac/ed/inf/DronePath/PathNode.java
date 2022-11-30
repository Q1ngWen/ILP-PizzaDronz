package uk.ac.ed.inf.DronePath;

public class PathNode {
    private final LngLat value;
    private double fScore;
    private double gScore;
    private double hScore;
    private PathNode previous = null;
    private double angleToPrevious;


    public PathNode(LngLat value) {
        this.value = value;
    }

    // setters
    public void setgScore(double gScore) {
        this.gScore = gScore;
    }

    public void sethScore(LngLat goal) {
        if (value.lng() == goal.lng() && value.lat() == goal.lat()){
            this.hScore = 0;
        } else {
            this.hScore = value.distanceTo(goal);
        }
    }

    public void setfScore(double fScore) {
        this.fScore = fScore;
    }

    public void setPrevious(PathNode previous) {
        this.previous = previous;
    }

    public void setAngleToPrevious(double angleToPrevious) {
        this.angleToPrevious = angleToPrevious;
    }

    // getters
    public LngLat getValue() {
        return value;
    }

    public double getgScore() {
        return gScore;
    }

    public double getfScore() {
        return fScore;
    }

    public double gethScore() {
        return hScore;
    }

    public PathNode getPrevious() {
        return previous;
    }

    public double getAngleToPrevious() {
        return angleToPrevious;
    }
}
