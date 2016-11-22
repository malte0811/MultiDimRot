CPPC=clang++
FLAGS= -std=c++11 -I ./include
SRC=src/multiDimRot/polytope/*.cpp src/multiDimRot/math/*.cpp src/multiDimRot/*.cpp src/*.cpp
LIBS = -lGL -lsfml-system -lsfml-window -lsfml-graphics -lGLEW -lboost_system -lboost_thread

MultiDimRot2.0: ${SRC}
	${CPPC} -o MultiDimRot2.0 ${SRC} ${FLAGS} ${LIBS} 
all: MultiDimRot2.0
