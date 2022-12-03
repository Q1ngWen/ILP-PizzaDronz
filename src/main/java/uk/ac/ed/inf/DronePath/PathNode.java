package uk.ac.ed.inf.DronePath;

import uk.ac.ed.inf.Map.CentralArea;
import uk.ac.ed.inf.Map.LngLat;
import uk.ac.ed.inf.Map.NoFlyZone;

/**
 * {@link PathNode} helps represent {@link LngLat} details like a graphical node. This helps link coordinates and
 * store heuristic values for {@link PathGenerator#AStarSearch(NoFlyZone[], CentralArea, PathNode, PathNode)}.
 *
 * @see <a href="https://www.sciencedirect.com/topics/engineering/graph-node">Here</a> for further detail
 * about nodes in graph theories.
 */
public class PathNode {
    private final LngLat value;
    private double fScore;
    private double gScore;
    private double hScore;
    private PathNode previous = null;


    /**
     * @param value Stores {@link LngLat} value in {@link PathNode}.
     */
    public PathNode(LngLat value) {
        this.value = value;
    }

    /**
     * Sets the g-heuristic value for the {@link PathNode}.
     *
     * @param gScore g-heuristic value for {@link PathGenerator#AStarSearch(NoFlyZone[], CentralArea, PathNode, PathNode)}
     */
    public void setgScore(double gScore) {
        this.gScore = gScore;
    }

    /**
     * Sets the h-heuristic value for the {@link PathNode}.
     *
     * @param goal {@link LngLat} coordinate of the end goal.
     */
    public void sethScore(LngLat goal) {
        if (value.lng() == goal.lng() && value.lat() == goal.lat()) {
            this.hScore = 0;
        } else {
            this.hScore = value.distanceTo(goal);
        }
    }

    /**
     * Sets the f-heuristic value for the {@link PathNode}.
     *
     * @param fScore Combination of {@link PathNode#gScore} and {@link PathNode#hScore}.
     */
    public void setfScore(double fScore) {
        this.fScore = fScore;
    }

    /**
     * Links the current {@link PathNode} to the previous visited {@link PathNode}.
     *
     * @param previous last visited {@link PathNode}.
     */
    public void setPrevious(PathNode previous) {
        this.previous = previous;
    }

    /**
     * @return Returns the {@link LngLat} value.
     */
    public LngLat getValue() {
        return value;
    }

    /**
     * @return Returns the g-heuristic score.
     */
    public double getgScore() {
        return gScore;
    }

    /**
     * @return Returns the f-heuristic score.
     */
    public double getfScore() {
        return fScore;
    }

    /**
     * @return Returns the h-heuristic score.
     */
    public double gethScore() {
        return hScore;
    }

    /**
     * @return Returns the previously visited {@link PathNode}.
     */
    public PathNode getPrevious() {
        return previous;
    }

}
