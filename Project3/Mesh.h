#pragma once
#include "header.h"
#include "Texture.h"

struct Vertex {
	glm::vec3 Position;
	glm::vec3 Normal;
	glm::vec3 TexCoords;
};

class Mesh
{
public:
	std::vector<Vertex> vertices;
	std::vector<unsigned int> indices;
	std::vector<Texture> textures;
	

	Mesh(Vertex* vertices, std::vector<unsigned int> indices, std::vector<Texture> textures);
	~Mesh();
private:
	unsigned int vao, vbo, ebo;
	void setupMesh();
};

