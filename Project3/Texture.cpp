#include "Texture.h"

Texture::Texture(unsigned int id, std::string type) {
	this->id = id;
	this->type = type;
}

Texture::~Texture() {

}

unsigned int Texture::getId() {
	return this->id;
}

std::string Texture::getType() {
	return this->type;
}
