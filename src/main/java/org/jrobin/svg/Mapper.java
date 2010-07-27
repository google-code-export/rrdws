/* ============================================================
 * JRobin : Pure java implementation of RRDTool's functionality
 * ============================================================
 *
 * Project Info:  http://www.jrobin.org
 * Project Lead:  Sasa Markovic (saxon@jrobin.org)
 *
 * Developers:    Sasa Markovic (saxon@jrobin.org)
 *
 *
 * (C) Copyright 2003-2005, by Sasa Markovic.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jrobin.svg;

class Mapper {
	private RrdGraphDef gdef;
	private ImageParameters im;
	private double pixieX, pixieY;

	Mapper(RrdGraph rrdGraph) {
		this.gdef = rrdGraph.gdef;
		this.im = rrdGraph.im;
		pixieX = (double) im.xsize / (double) (im.end - im.start);
		if (!gdef.logarithmic) {
			pixieY = (double) im.ysize / (im.maxval - im.minval);
		}
		else {
			pixieY = (double) im.ysize / (Math.log10(1.0000001+im.maxval) - Math.log10(1.0000001+im.minval));
		}
	}

	int xtr(double mytime) {
		return (int) ((double) im.xorigin + pixieX * (mytime - im.start));
	}

	int ytr(double value) {
		double yval;
		if (!gdef.logarithmic) {
			yval = im.yorigin - pixieY * (value - im.minval) + 0.5;
		}
		else {
			if (value < im.minval) {
				yval = im.yorigin;
			}
			else {
				yval = im.yorigin - pixieY * (Math.log10(1.0000001+value) - Math.log10(1.0000001+im.minval)) + 0.5;
			}
		}
		if (!gdef.rigid) {
			return (int) yval;
		}
		else if ((int) yval > im.yorigin) {
			return im.yorigin + 2;
		}
		else if ((int) yval < im.yorigin - im.ysize) {
			return im.yorigin - im.ysize - 2;
		}
		else {
			return (int) yval;
		}
	}
}
