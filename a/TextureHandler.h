#pragma once
#include <iostream>
#include <vector>
class TextureHandler
{
	std::vector<unsigned int> images;
public:
	TextureHandler();
	std::vector<unsigned int> getImages();
};
