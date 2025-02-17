#pragma once
#include <iostream>

class Texture
{
	unsigned int id;
	std::string type;
	std::string path;
public:
	Texture(unsigned int id, std::string& type, std::string& filepath);
	Texture(const char* type);
	Texture();
	~Texture();
	unsigned int getId();
	std::string getType();
	std::string getPath();
	void setId(unsigned int id1);
	void setPath(std::string path);
	void setType(std::string type);
};

