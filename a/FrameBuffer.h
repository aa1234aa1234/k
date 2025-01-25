#pragma once

class FrameBuffer
{
	unsigned int fbo, texture;
public:
	FrameBuffer(float width, float height);
	FrameBuffer();
	~FrameBuffer();
	unsigned int getFrameTexture();
	unsigned int getFrameBuffer();
	void bind();
	void unbind();
};
