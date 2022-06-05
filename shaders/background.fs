#version 460 core

layout(binding = 0) uniform sampler2D Image;

out vec4 out_Color;

in vec2 texCoords;

void main(){
	out_Color = texture(Image, texCoords);
}