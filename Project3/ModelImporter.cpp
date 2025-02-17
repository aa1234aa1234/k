#define STB_IMAGE_IMPLEMENTATION
#include <stb/stb_image.h>
#include "header.h"


unsigned int ModelImporter::getTextureFromFile(const char* filepath, bool gamma) {
	std::string filename = std::string(filepath);
	//filename = filename.replace(0, 0, directory);
	unsigned int id;
	glGenTextures(1, &id);
	int width, height, components;
	unsigned char* data = stbi_load(filename.c_str(), &width, &height, &components, 0);
	if (data) {
		GLenum format;
		switch (components) {
		case 1:
			format = GL_RED;
			break;
		case 3:
			format = GL_RGB;
			break;
		case 4:
			format = GL_RGBA;
			break;
		}
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		stbi_image_free(data);
		//glBindTexture(GL_TEXTURE_2D, 0);
	}
	else {
		std::cout << "texture failed to load " << filepath << std::endl;
		stbi_image_free(data);
	}

	return id;
}