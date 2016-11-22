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
#ifndef SRC_MULTIDIMROT_MATH_THREADEDMATVECMULTIPLIER_H_
#define SRC_MULTIDIMROT_MATH_THREADEDMATVECMULTIPLIER_H_

#include <array>
#include <MatrixNxN.h>
#include <VecN.h>
#include <vector>
#include <boost/thread.hpp>
#include <boost/thread/mutex.hpp>
#include <thread>

class ThreadedMatVecMultiplier {
public:
	ThreadedMatVecMultiplier(const MatrixNxN& m, int threadCount);
	virtual ~ThreadedMatVecMultiplier();
	void apply(const std::vector<VecN>& in, std::vector<VecN>& out, bool normals);
	void stop();
private:
	int threadCount = 4;
	int vertexCount = -1;
	int nextVertex = 0;
	int activeCount = 0;
	const MatrixNxN& matrix;
	const std::vector<VecN>* input = 0;
	std::vector<VecN>* output = 0;
	std::vector<boost::thread> threads;
	boost::mutex mutex;
	boost::condition_variable condition;
	bool normals;
	void run(int threadId);
};

#endif /* SRC_MULTIDIMROT_MATH_THREADEDMATVECMULTIPLIER_H_ */
