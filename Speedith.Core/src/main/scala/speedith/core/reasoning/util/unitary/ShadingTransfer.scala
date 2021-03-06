package speedith.core.reasoning.util.unitary

import speedith.core.lang.{Region, TransformationException, Zone, PrimarySpiderDiagram}
import java.util
import scala.collection.JavaConversions._

case class ShadingTransfer(sourceDiagram: PrimarySpiderDiagram, destinationDiagram: PrimarySpiderDiagram) {

  def transferShading(shadedZones: Zone*): PrimarySpiderDiagram = {
    assertZonesInSourceShaded(shadedZones)

    val correspondingRegion = CorrespondingRegions(sourceDiagram, destinationDiagram).correspondingRegion(new Region(shadedZones))

    destinationDiagram.addShading(correspondingRegion.zones)
  }

  def transferShading(shadedZones: util.Collection[Zone]): PrimarySpiderDiagram = {
    transferShading(shadedZones.toSeq:_*)
  }

  private def assertZonesInSourceShaded(zones: Seq[Zone]): Unit = {
    val nonShadedZone = zones.find(!sourceDiagram.getShadedZones.contains(_))
    if (nonShadedZone.isDefined) {
      throw new TransformationException("The zone '" + nonShadedZone.get + "' is not shaded in the source unitary diagram.")
    }
  }

}
