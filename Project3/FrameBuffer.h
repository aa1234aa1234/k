#pragma once

class FrameBuffer
{
	unsigned int fbo,texture,rbo;
public:
	FrameBuffer(double width, double height);
	FrameBuffer();
	~FrameBuffer();
	unsigned int getFrameTexture();
	unsigned int getFrameBuffer();
	void bind();
	void unbind();
};

