#ifndef CYCLEDLETTERSEQUENCE_H#define CYCLEDLETTERSEQUENCE_H#include <string>using std::string;class CycledLetterSequence {public:	CycledLetterSequence();		CycledLetterSequence(std::string str);		char charAtIndex(int index);		string toString();	private:	static char letters[];	std::string seq;};#endif