/*
 *   Project: Speedith.Core
 * 
 * File name: Isabelle2010ExportProvider.java
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
package speedith.core.lang.export;

import speedith.core.lang.reader.ReadingException;
import speedith.core.lang.reader.SpiderDiagramsReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import speedith.core.lang.NarySpiderDiagram;
import speedith.core.lang.NullSpiderDiagram;
import speedith.core.lang.PrimarySpiderDiagram;
import static speedith.core.i18n.Translations.i18n;
import speedith.core.lang.Region;
import speedith.core.lang.SpiderDiagram;
import speedith.core.lang.Zone;
import speedith.core.util.Sets;
import static speedith.core.lang.Operator.*;

/**
 * The provider for exporting spider diagrams to Isabelle 2011 formulae.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Isabelle2010ExportProvider extends SDExportProvider {

    /**
     * The name of the export format of this provider.
     */
    public static final String FormatName = "Isabelle2010";

    @Override
    public String getFormatName() {
        return FormatName;
    }

    @Override
    public SDExporter getExporter(Map<String, Object> parameters) {
        return new Exporter();
    }

    @Override
    public String getDescription(Locale locale) {
        return i18n(locale, "ISABELE_EXPORT_DESCRIPTION");
    }

    private static class Exporter extends SDExporter {

        @Override
        public void exportTo(SpiderDiagram sd, Writer output) throws IOException {
            if (output == null) {
                throw new IllegalArgumentException(i18n("GERR_NULL_ARGUMENT", "output"));
            }
            exportDiagram(sd, output);
        }

        private void exportNullDiagram(Writer output) throws IOException {
            output.append("True");
        }

        private void exportNaryDiagram(NarySpiderDiagram nsd, Writer output) throws IOException {
            final String opName = nsd.getOperator().getName();
            if (OP_NAME_IMP.equals(opName)) {
                for (SpiderDiagram sd : nsd.getOperands()) {
                    exportDiagram(sd, output);
                    output.append('\n');
                }
            }
        }

        private void exportPrimaryDiagram(PrimarySpiderDiagram psd, Writer output) throws IOException {
            output.append('(');
            SortedSet<String> spiders = psd.getSpiders();
            if (psd.getSpidersCount() > 0) {
                Iterator<String> itr = spiders.iterator();
                output.append("\\<exists>").append(itr.next());
                while (itr.hasNext()) {
                    output.append(' ').append(itr.next());
                }
                output.append(". ");
            }
            if (psd.getHabitatsCount() < 1 && psd.getShadedZonesCount() < 1) {
                output.append("True");
            } else {
                if (psd.getHabitatsCount() > 0) {
                    SortedMap<String, Region> habitats = psd.getHabitats();
                    Iterator<String> itr = habitats.keySet().iterator();
                    String spider = itr.next();
                    exportHabitat(spider, habitats.get(spider), output);
                    while (itr.hasNext()) {
                        spider = itr.next();
                        output.append(" \\<and> ");
                        exportHabitat(spider, habitats.get(spider), output);
                    }
                    if (psd.getShadedZonesCount() > 0) {
                        output.append(" \\<and> ");
                    }
                }
                if (psd.getShadedZonesCount() > 0) {
                    SortedSet<Zone> shadedZones = psd.getShadedZones();
                    Iterator<Zone> itr = shadedZones.iterator();
                    Zone zone = itr.next();
                    exportZone(zone, output);
                    output.append(" \\<subseteq> ");
                    Sets.printSet(spiders, output);
                }
            }
            output.append(')');
        }

        private void exportDiagram(SpiderDiagram sd, Writer output) throws IOException {
            if (sd instanceof NullSpiderDiagram) {
                exportNullDiagram(output);
            } else if (sd instanceof NarySpiderDiagram) {
                exportNaryDiagram((NarySpiderDiagram) sd, output);
            } else if (sd instanceof PrimarySpiderDiagram) {
                exportPrimaryDiagram((PrimarySpiderDiagram) sd, output);
            } else {
                throw new IllegalArgumentException(i18n("ERR_EXPORT_INVALID_SD"));
            }
        }

        private void exportHabitat(String spider, Region region, Writer output) throws IOException {
            output.append(spider).append(" \\<in> ");
            exportRegion(region, output);
        }

        private void exportRegion(Region region, Writer output) throws IOException {
            SortedSet<Zone> zones = region.getZones();
            if (zones.isEmpty()) {
                output.append("UNIV");
            } else {
                Iterator<Zone> itr = zones.iterator();
                Zone zone = itr.next();
                boolean needsParens = zone.getInContoursCount() > 1 || zone.getOutContoursCount() > 1 || (zone.getInContoursCount() == 1 && zone.getOutContoursCount() == 1);
                if (needsParens && zones.size() > 1) {
                    output.append('(');
                }
                exportZone(zone, output);
                if (needsParens && zones.size() > 1) {
                    output.append(')');
                }
                while (itr.hasNext()) {
                    zone = itr.next();
                    output.append(" \\<union> ");
                    if (needsParens) {
                        output.append('(');
                    }
                    exportZone(zone, output);
                    if (needsParens) {
                        output.append(')');
                    }
//                    output.append(')');
                }
            }
        }

        private void exportZone(Zone zone, Writer output) throws IOException {
            SortedSet<String> inContours = zone.getInContours();
            SortedSet<String> outContours = zone.getOutContours();
            boolean someOuts = outContours != null && !outContours.isEmpty();
            boolean someIns = inContours != null && !inContours.isEmpty();
            if (someIns) {
                Iterator<String> itr = inContours.iterator();
                boolean needsParens = someOuts && inContours.size() > 1;
                if (needsParens) {
                    output.append('(');
                }
                output.append(itr.next());
                while (itr.hasNext()) {
                    output.append(" \\<inter> ").append(itr.next());
                }
                if (needsParens) {
                    output.append(')');
                }
                if (someOuts) {
                    output.append(" - ");
                }
            }
            if (someOuts) {
                Iterator<String> itr = outContours.iterator();
                boolean needsParens = someIns && outContours.size() > 1;
                if (needsParens) {
                    output.append('(');
                }
                output.append(itr.next());
                while (itr.hasNext()) {
                    output.append(" \\<union> ").append(itr.next());
                }
                if (needsParens) {
                    output.append(")");
                }
            }
        }
    }
//    private static class OperatorInfo {
//        public static final int Infix = 1;
//        public static final int Prefix = 2;
//        public static final int Suffix = 3;
//
//        public final int type;
//        public final String xSymbolName;
//        public final String name;
//
//        public OperatorInfo(int type, String xSymbolName, String name) {
//            this.type = type;
//            this.xSymbolName = xSymbolName;
//            this.name = name;
//        }
//    }

    public static void main(String[] args) throws ReadingException {
        SDExporter exporter = SDExporting.getExporter(Isabelle2010ExportProvider.FormatName);
        SpiderDiagram sd = SpiderDiagramsReader.readSpiderDiagram("BinarySD {arg1 = PrimarySD { spiders = [\"s\", \"s'\"], sh_zones = [([\"A\", \"B\"],[\"C\", \"D\"])], habitats = [(\"s\", [([\"A\", \"B\"], [])]), (\"s'\", [([\"A\"], [\"B\"]), ([\"B\"], [\"A\"])])]}, arg2 = PrimarySD {spiders = [\"s\", \"s'\"], habitats = [(\"s\", [([\"A\"], [])]), (\"s'\", [([\"B\"], [])])], sh_zones = []}, operator = \"op -->\" }");
        String sdStr = exporter.export(sd);
        System.out.println(sdStr);
    }
}
