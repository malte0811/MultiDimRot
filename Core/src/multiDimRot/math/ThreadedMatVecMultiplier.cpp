/*******************************************************************************
 * This file is part of MultiDimRot2.0.
 * Copyright (C) 2016, 2017 malte0811
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

#include <multiDimRot/math/ThreadedMatVecMultiplier.h>

using namespace MultiDimRot::Math;

ThreadedMatVecMultiplier::ThreadedMatVecMultiplier(const MatrixNxN& m, int tC): threadCount(tC), matrix(m) {
	if (threadCount>1) {
		threads = std::vector<boost::thread>(threadCount);
		for (int i = 0;i<threads.size();i++) {
			threads[i] = boost::thread(boost::bind( &ThreadedMatVecMultiplier::run, this, i));
		}
	}
}

ThreadedMatVecMultiplier::~ThreadedMatVecMultiplier() {
	if (output!=0) {
		delete output;
	}
}

void ThreadedMatVecMultiplier::apply(const std::vector<VecN>& in, std::vector<VecN>& out, bool normals) {
	if (threadCount<=1) {
		if (normals) {
			matrix.applyInvTMass(in, out);
		} else {
			matrix.applyMass(in, out);
		}
	} else {
		boost::mutex::scoped_lock lock(mutex);
		this->normals = normals;
		input = &in;
		output = &out;
		while (output->size()<in.size()) {
			output->push_back(VecN(matrix.getSize()));
		}
		vertexCount = in.size();
		nextVertex = 0;
		condition.notify_all();
		while (nextVertex<vertexCount || activeCount>0) {
			condition.wait(lock);
		}
	}
}

void ThreadedMatVecMultiplier::run(int threadId) {
	int myNext;
	while (!boost::this_thread::interruption_requested()) {
		{
			boost::mutex::scoped_lock lock(mutex);
			while (nextVertex>=vertexCount) {
				condition.wait(lock);
			}
			if (output==0) {
				return;
			}
			activeCount++;
		}
		//TODO maybe get rid of while (true)
		while (true) {
			{
				boost::mutex::scoped_lock lock(mutex);
				myNext = nextVertex;
				nextVertex++;
				if (myNext>=vertexCount) {
					break;
				}
			}
			if (normals) {
				matrix.applyInvT((*input)[myNext], (*output)[myNext]);
			} else {
				matrix.apply((*input)[myNext], (*output)[myNext]);
			}
		}
		{
			boost::mutex::scoped_lock lock(mutex);
			activeCount--;
			condition.notify_all();
		}
	}
}
void ThreadedMatVecMultiplier::stop() {
	{
		boost::mutex::scoped_lock lock(mutex);
		output = 0;
		nextVertex = 0;
		vertexCount = 100;
		condition.notify_all();
	}
	for (int i = 0;i<threads.size();i++) {
		if (threads[i].joinable()) {
			threads[i].join();
		}
	}
}
