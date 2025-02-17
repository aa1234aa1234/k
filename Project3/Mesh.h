#pragma once
#include "header.h"
#include "Texture.h"
#include "Shader.h"

class Mesh
{
public:
	std::vector<Vertex> vertices;
	std::vector<unsigned int> indices;
	std::vector<Texture> textures;
	

	Mesh(std::vector<Vertex> vertices, std::vector<unsigned int> indices, std::vector<Texture> textures);
	Mesh();
	~Mesh();
	void Draw(Shader& shader);
private:
	unsigned int vao, vbo, ebo;
	void setupMesh();
};

