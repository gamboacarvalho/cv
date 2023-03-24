package gpadUml;

import gpad.model.CompositeShape;
import gpad.model.INode;
import gpad.model.INodeFactory;
import gpad.model.IShape;
import gpadGd.NodeCircle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

/**
   A UML class diagram.
 */
public class UMLTools{
	public static IShape [] getShapePrototypes()
	{
		ClassRelationshipEdge rel;
		IShape [] e = new IShape[7];
		e[0] = new ClassNode();

		e[1] = rel = new ClassRelationshipEdge();
		rel.setLineStyle(LineStyle.DOTTED);
		rel.setEndArrowHead(ArrowHead.V);
		
		e[2] = rel = new ClassRelationshipEdge();
		rel.setBentStyle(BentStyle.VHV);
		rel.setEndArrowHead(ArrowHead.TRIANGLE);

		e[3] = rel = new ClassRelationshipEdge();
		rel.setBentStyle(BentStyle.VHV);
		rel.setLineStyle(LineStyle.DOTTED);
		rel.setEndArrowHead(ArrowHead.TRIANGLE);

		e[4] = rel = new ClassRelationshipEdge();
		rel.setBentStyle(BentStyle.HVH);
		rel.setEndArrowHead(ArrowHead.TRIANGLE);

		e[5] = rel = new ClassRelationshipEdge();
		rel.setBentStyle(BentStyle.HVH);
		rel.setStartArrowHead(ArrowHead.DIAMOND);

		e[6] = rel = new ClassRelationshipEdge();
		rel.setBentStyle(BentStyle.HVH);
		rel.setStartArrowHead(ArrowHead.BLACK_DIAMOND);

		return e;
	}
}





