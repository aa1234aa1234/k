#include "Texture.h"

Texture::Texture(unsigned int id, std::string& type, std::string& filepath) {
	this->id = id;
	this->type = type;
	this->path = filepath;
}

Texture::Texture(const char* type) : type(type) {

}

Texture::~Texture() {

}

unsigned int Texture::getId() {
	return this->id;
}

std::string Texture::getType() {
	return this->type;
}

std::string Texture::getPath() {
	return this->path;
}

void Texture::setId(unsigned int id1) {
	this->id = id1;
}
