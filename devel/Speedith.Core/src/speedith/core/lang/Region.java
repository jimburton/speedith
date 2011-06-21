/*
 *   Project: Speedith.Core
 * 
 * File name: Region.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2011 Matej Urbas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package speedith.core.lang;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import speedith.core.util.Sets;
import static speedith.core.i18n.Translations.i18n;

/**
 * This class represents a region in spider diagrams.
 * <p>A region is a union of zones. Thus the {@link Region} class contains 
 * {@link Region#getZones() a set of zones} which constitute it.</p>
 * <p>Instances of this class (and its derived classes) are immutable.</p>
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Region {

    // <editor-fold defaultstate="collapsed" desc="Private Fields">
    private TreeSet<Zone> m_zones;
    private boolean hashInvalid = true;
    private int hash;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new region from the given collection of zones. The resulting
     * region will constitute of these zones.
     * <p>Note that duplicate zones in the given collection will be ignored.</p>
     * @param zones the collection of zones from which to construct this region.
     * <p>This argument may be {@code null}. This indicates an empty region.</p>
     */
    public Region(Collection<Zone> zones) {
        this(zones == null ? null : new TreeSet<Zone>(zones));
    }

    /**
     * Creates a new region from the given collection of zones. The resulting
     * region will constitute of these zones.
     * <p><span style="font-weight:bold">Important</span>: this method does
     * not make a copy of the given zone set. Hence, it is possible to violate
     * the immutability property of this class (which means that the
     * contract for the {@link Zone#hashCode()} method might be broken). So,
     * make sure that you do not change the given set after creating this region
     * with it.</p>
     * <p>Note that duplicate zones in the given collection will be ignored.</p>
     * @param zones the collection of zones from which to construct this region.
     * <p>This argument may be {@code null}. This indicates an empty region.</p>
     */
    public Region(TreeSet<Zone> zones) {
//        if (zones != null && zones.contains(null)) {
//            throw new RuntimeException(i18n("ERR_NULL_ZONE_IN_REGION"));
//        }
        this.m_zones = zones;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
    /**
     * Returns a set of {@link Zone zones} which make up this region.
     * <p>Note: this method may return {@code null}, which indicates an empty
     * region.</p>
     * @return a set of {@link Zone zones} which make up this region.
     * <p>Note: this method may return {@code null}, which indicates an empty
     * region.</p>
     */
    public SortedSet<Zone> getZones() {
        return m_zones == null || m_zones.isEmpty() ? null : Collections.unmodifiableSortedSet(m_zones);
    }

    /**
     * Returns the number of {@link Region#getZones() zones} in this region.
     * @return the number of {@link Region#getZones() zones} in this region.
     */
    public int getZonesCount() {
        return m_zones == null ? 0 : m_zones.size();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Equality">
    /**
     * Two regions equal if they constitute of the same zones.
     * @param obj the object with which to compare this region.
     * @return {@code true} if and only if {@code obj} is a region and it
     * contains the same zones.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Region) {
            return Sets.equal(m_zones, ((Region) obj).m_zones);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (hashInvalid) {
            hash = (m_zones == null || m_zones.isEmpty() ? 0 : m_zones.hashCode());
            hashInvalid = false;
        }
        return hash;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Text Conversion Methods">
    void toString(StringBuilder sb) {
        if (sb == null) {
            throw new IllegalArgumentException(i18n("GERR_NULL_ARGUMENT", "sb"));
        }
        SpiderDiagram.printZoneList(sb, m_zones);
    }
    // </editor-fold>
}
