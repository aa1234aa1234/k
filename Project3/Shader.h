#pragma once
#include "header.h"
#include <fstream>
#include <string>
class Shader
{
	unsigned int id;
public:
	Shader(const char* filepath1, const char* filepath2);
	~Shader();
	void use();
	unsigned int getId();
};

