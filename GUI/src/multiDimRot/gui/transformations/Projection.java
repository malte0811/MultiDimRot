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

public class Projection extends Transformation {
	int dimension;
	float distance;
	// d<=0 means project down to 2d
	public Projection(int d, float dist) {
		dimension = d;
		distance = dist;
	}
	@Override
	public String getHumanString(boolean multiple) {
		if (dimension>0) {
			return "Project onto a "+(dimension-1)+" dimensional space from distance "+distance;
		} else {
			return "Project down to 2 dimensions from distance "+distance;
		}
	}

	@Override
	public String getParameterString(boolean multiple) {
		if (dimension>0) {
			return "project "+dimension+" "+distance;
		} else {
			return "projectAll "+distance;
		}
	}

	@Override
	public boolean isValid(int dims) {
		return dimension<=dims&&distance>0;
	}

}
