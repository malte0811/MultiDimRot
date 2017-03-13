CPPC=clang++
FLAGS= -std=c++11 -I ./include
GUICOPY=../GUI/MultiDimRot2.0

SRC=$(shell find -name "*.cpp")
OBJC=$(SRC:./src/%.cpp=./bin/%.o)

LIBS = -lGL -lsfml-system -lsfml-window -lsfml-graphics -lGLEW -lboost_system -lboost_thread

MultiDimRot2.0: ${OBJC}
	@echo linking
	${CPPC} -o MultiDimRot2.0 ${OBJC} ${FLAGS} ${LIBS}
	cp MultiDimRot2.0 ${GUICOPY}

all: MultiDimRot2.0

bin/%.o: src/%.cpp
	@echo
	@echo building $@
	mkdir -p ${dir $@} 
	${CPPC} ${FLAGS} -c -o $@ $<
	@echo

clean:
	rm -f MultiDimRot2.0
	rm -rf bin

.PHONY: clean
