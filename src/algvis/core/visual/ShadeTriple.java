package algvis.core.visual;

import java.awt.geom.Rectangle2D;

import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.View;

public class ShadeTriple extends VisualElement {
	BSTNode u, v, w;

	public ShadeTriple(BSTNode u, BSTNode v, BSTNode w) {
		super(Scene.MAXZ - 1);
		this.u = u;
		this.v = v;
		this.w = w;
	}

	@Override
	protected void draw(View V) {
		BSTNode z;
		if (u != null) {
			z = u.getParent();
			if (z == v || z == w) {
				V.drawWideLine(u.position.relative, z.position.relative);
			}
		}
		if (v != null) {
			z = v.getParent();
			if (z == u || z == w) {
				V.drawWideLine(v.position.relative, z.position.relative);
			}
		}
		if (w != null) {
			z = w.getParent();
			if (z == u || z == v) {
				V.drawWideLine(w.position.relative, z.position.relative);
			}
		}
	}

	@Override
	protected void move() {
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null;
	}
}
