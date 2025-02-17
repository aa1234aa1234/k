#pragma once
#include "header.h"
#include <string>

#include <fstream>
#include <sstream>
#include <map>
#include "Texture.h"

struct Vertex {
	glm::vec3 Position;
	glm::vec3 Normal;
	glm::vec2 TexCoords;
};

struct Material {
	std::string name;
	glm::vec3 ambiant;
	glm::vec3 diffuse;
	glm::vec3 specular;
	float shininess;
	Texture texture;
	Material() : ambiant(glm::vec3(0.0f, 0.0f, 0.0f)), diffuse(glm::vec3(0.0f, 0.0f, 0.0f)), specular(glm::vec3(0.0f, 0.0f, 0.0f)) {

	}
};



class ModelImporter {
public:
	ModelImporter(const char* filepath) {
		std::ifstream stream(filepath, std::ios::in);
		std::string line;
		std::vector<glm::vec3> position;
		std::vector<glm::vec2> texcoord;
		std::vector<glm::vec3> normal;
		std::vector<unsigned int> indices;
		std::map<std::string,Material> mats;
		std::string str = std::string(filepath);
		std::strtok((char*)str.c_str(), "/");
		const char* directory = str.c_str();
		
		while (std::getline(stream, line)) {
			std::istringstream iss(line);
			std::string a;
			iss >> a;
			if (a == "mtllib") {
				std::string file;
				iss >> file;
				loadMaterials(file.c_str(), directory, mats);
			}
			if (a == "v") {
				glm::vec3 vec;
				iss >> vec.x >> vec.y >> vec.z;
				position.push_back(vec);
			}
			if (a == "vt") {
				glm::vec2 texvec;
				iss >> texvec.x >> texvec.y;
				texcoord.push_back(texvec);
			}
			if (a == "vn") {
				glm::vec3 nvec;
				iss >> nvec.x >> nvec.y >> nvec.z;
				normal.push_back(nvec);
			}
		}
	}
	
	void loadMaterials(const char* filename, const char* directory, std::map<std::string,Material> &mats) {
		std::ifstream stream(std::string(directory) + '/' + std::string(filename).c_str(), std::ios::in);
		std::string line;
		Material currentmat;
		while(std::getline(stream, line)) {
			std::istringstream iss(line);
			std::string a;
			iss >> a;
			if (a == "newmtl") {
				std::string name;
				iss >> name;
				if (!currentmat.name.empty()) {
					mats[currentmat.name] = currentmat;
				}
				iss >> currentmat.name;
			}
			else if (a == "Ka") {
				iss >> currentmat.ambiant.x >> currentmat.ambiant.y >> currentmat.ambiant.z;
			}
			else if (a == "Kd") {
				iss >> currentmat.diffuse.x >> currentmat.diffuse.y >> currentmat.diffuse.z;
			}
			else if (a == "Ks") {
				iss >> currentmat.specular.x >> currentmat.specular.y >> currentmat.specular.z;
			}
			else if (a == "Ns") {
				iss >> currentmat.shininess;
			}
			else if (a == "map_Kd") {
				std::string file;
				iss >> file;
				file = file.replace(0, 0, directory);
				currentmat.texture.setPath(file);
				currentmat.texture.setType("diffuse");
				currentmat.texture.setId(getTextureFromFile(file.c_str()));
			}
			else if (a == "map_Ks") {
				std::string file;
				iss >> file;
				file = file.replace(0, 0, directory);
				currentmat.texture.setPath(file);
				currentmat.texture.setType("specular");
				currentmat.texture.setId(getTextureFromFile(file.c_str()));
			}
		}
		//for (auto& p : mats) {
		//	std::cout << p.second.name << ' ' << p.second.shininess << ' ' << p.second.texture.getPath() << ' ' << p.second.texture.getType() << std::endl;
		//}
	}

	unsigned int getTextureFromFile(const char* filepath, bool gamma = false);
};