#pragma once
#include <iostream>
#include <vector>
class ImageHandler
{
	std::vector<unsigned int> images;
public:
	ImageHandler();
	std::vector<unsigned int> getImages();
};

