/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016 malte0811
 *
 * MultiDimRot2.0 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MultiDimRot2.0 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MultiDimRot2.0.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package multiDimRot.gui.transformations;

public class Rotation extends Transformation {
	final int a0, a1;
	final float angle;
	final boolean inc;
	public Rotation(int ax0, int ax1, float a, boolean i) {
		a0 = ax0;
		a1 = ax1;
		angle = a;
		inc = i;
	}
	@Override
	public String getHumanString(boolean multiple) {
		return "Rotate in the plane "+a0+"|"+a1+"\n by "+angle+"°"+(inc?"\n per frame (40 FPS)":"");
	}
	@Override
	public String getParameterString(boolean matVec) {
		if (matVec) {
			return (inc?"rotInc ":"rotConst ")+a0+" "+a1+" "+angle;
		} else {
			return "rotate "+a0+" "+a1+" "+angle;
		}
	}
	@Override
	public boolean isValid(int dims) {
		return a0<dims&&a1<dims;
	}
}
