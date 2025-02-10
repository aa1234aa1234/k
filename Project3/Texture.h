#pragma once
#include <iostream>

class Texture
{
	unsigned int id;
	std::string type;
	std::string path;
public:
	Texture(unsigned int id, std::string& type, std::string& filepath);
	~Texture();
	unsigned int getId();
	std::string getType();
	std::string getPath();
};

