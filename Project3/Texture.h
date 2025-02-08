#pragma once
#include <iostream>
class Texture
{
	unsigned int id;
	std::string type;
public:
	Texture(unsigned int id, std::string type);
	~Texture();
	unsigned int getId();
	std::string getType();
};

