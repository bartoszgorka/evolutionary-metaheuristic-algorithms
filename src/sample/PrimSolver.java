package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class PrimSolver {
    private double penalties;
    private ArrayList<PointsPath> path;

    /**
     * Get penalties sum - total distance
     *
     * @return Penalties value - sum of distances in graph
     */
    public double getPenalties() {
        return penalties;
    }

    /**
     * Get prepared MST as list of points
     *
     * @return MST list
     */
    public ArrayList<PointsPath> getPath() {
        return this.path;
    }

    /**
     * Construct MST and calculate penalties.
     *
     * @param indexes        Indexes of nodes in group
     * @param distanceMatrix Distance matrix to build structure and use distance
     */
    public void construct(int[] indexes, double[][] distanceMatrix) {
        this.path = new ArrayList<>();
        int pointsToVisit = indexes.length;
        int visitedPoints;
        HashSet<Integer> visitedIndexes = new HashSet<>();

        // Prepare list with all possible paths (fragments) between points
        ArrayList<PointsPath> possiblePaths = new ArrayList<>();
        for (int first = 0; first < pointsToVisit; first++) {
            for (int second = first + 1; second < pointsToVisit; second++) {
                possiblePaths.add(new PointsPath(first, second, distanceMatrix[first][second]));
            }
        }

        // Order by min distance first
        Collections.sort(possiblePaths);

        // TODO debug only
        for (PointsPath p : possiblePaths) {
            System.out.println(p.toString());
        }

        // Add first point to path list
        PointsPath point = possiblePaths.remove(0);
        this.path.add(point);
        visitedPoints = 2;
        visitedIndexes.add(point.getStartIndex());
        visitedIndexes.add(point.getEndIndex());

        while (visitedPoints != pointsToVisit) {
            for (int i = 0; i < possiblePaths.size(); i++) {
                // Get point from list
                PointsPath p = possiblePaths.get(i);

                // If start or end location in already visited - use it
                boolean containsFirst = visitedIndexes.contains(p.getStartIndex());
                boolean containsEnd = visitedIndexes.contains(p.getEndIndex());

                if ((containsFirst && !containsEnd) || (!containsFirst && containsEnd)) {
                    point = possiblePaths.remove(i);
                    this.path.add(point);
                    visitedIndexes.add(point.getStartIndex());
                    visitedIndexes.add(point.getEndIndex());

                    // Increment visited points and break for loop
                    visitedPoints += 1;
                    break;
                }
            }
        }

        // TODO debug only
        System.out.println("----- PATH -----");
        for (PointsPath p : this.path) {
            System.out.println(p.toString());
        }

        // Calculate penalties
        this.calculatePenalties();
    }

    /**
     * Calculate sum of penalties and store it in object's state
     */
    private void calculatePenalties() {
        this.penalties = 0.0;
        for (PointsPath point : this.path) {
            this.penalties += point.getPenalties();
        }
    }
}