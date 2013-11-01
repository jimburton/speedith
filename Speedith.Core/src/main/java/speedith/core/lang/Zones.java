package speedith.core.lang;

import java.util.*;

public final class Zones {

    public static ArrayList<Zone> allZonesForContours(String... contours) {
        ArrayList<Zone> powerRegion = new ArrayList<>();
        for (String contour : contours) {
            addContourToPowerRegion(powerRegion, contour);
        }
        return powerRegion;
    }

    public static ArrayList<Zone> getZonesOutsideContours(Collection<Zone> region, String... contours) {
        ArrayList<Zone> zonesOutsideContours = new ArrayList<>();
        for (Zone zone : region) {
            if (!isZonePartOfAnyContour(zone, contours)) {
                zonesOutsideContours.add(zone);
            }
        }
        return zonesOutsideContours;
    }

    public static ArrayList<Zone> getZonesInsideContours(ArrayList<Zone> region, String... contours) {
        ArrayList<Zone> zonesInsideContours = new ArrayList<>();
        for (Zone zone : region) {
            if (isZonePartOfAnyContour(zone, contours)) {
                zonesInsideContours.add(zone);
            }
        }
        return zonesInsideContours;
    }

    public static SortedSet<Zone> extendRegionWithNewContour(Collection<Zone> region, String newContour) {
        if (region == null || region.isEmpty()) {
            return new TreeSet<>(Arrays.asList(Zone.fromInContours(newContour)));
        } else {
            List<Zone> extendedRegion = sameRegionWithNewContours(region, newContour);
            SortedSet<String> allContours = region.iterator().next().getAllContours();
            extendedRegion.addAll(sameRegionWithNewContours(Arrays.asList(Zone.fromInContours(newContour)), allContours.toArray(new String[allContours.size()])));
            return new TreeSet<>(extendedRegion);
        }
    }

    public static ArrayList<Zone> sameRegionWithNewContours(Collection<Zone> region, String... newContours) {
        if (region == null) {
            return new ArrayList<>();
        }
        ArrayList<Zone> updatedRegion = new ArrayList<>(region);
        for (String newContour : newContours) {
            int currentRegionSize = updatedRegion.size();
            for (int i = 0; i < currentRegionSize; i++) {
                Zone zone = updatedRegion.get(i);

                updatedRegion.set(i, createZoneWithAddedInContour(newContour, zone));
                updatedRegion.add(createZoneWithAddedOutContour(newContour, zone));
            }
        }
        return updatedRegion;
    }

    private static boolean isZonePartOfAnyContour(Zone zone, String[] contours) {
        for (String contour : contours) {
            if (zone.getInContoursCount() > 0 && zone.getInContours().contains(contour)) {
                return true;
            }
        }
        return false;
    }

    private static void addContourToPowerRegion(ArrayList<Zone> powerRegion, String contour) {
        if (powerRegion.isEmpty()) {
            powerRegion.add(Zone.fromInContours(contour));
            powerRegion.add(Zone.fromOutContours(contour));
        } else {
            int oldZonesCount = powerRegion.size();
            for (int i = 0; i < oldZonesCount; i++) {
                Zone zone = powerRegion.get(i);
                powerRegion.set(i, createZoneWithAddedInContour(contour, zone));
                powerRegion.add(createZoneWithAddedOutContour(contour, zone));
            }
        }
    }

    private static Zone createZoneWithAddedInContour(String newContour, Zone zone) {
        return new Zone(extendContours(newContour, zone.getInContours()), zone.getOutContours());
    }

    private static Zone createZoneWithAddedOutContour(String contour, Zone zone) {
        return new Zone(zone.getInContours(), extendContours(contour, zone.getOutContours()));
    }

    private static TreeSet<String> extendContours(String contour, SortedSet<String> oldInContours) {
        TreeSet<String> newContours = new TreeSet<>();
        if (oldInContours != null) {
            newContours.addAll(oldInContours);
        }
        newContours.add(contour);
        return newContours;
    }

}
