#version 460 core

uniform vec4 color;
layout(binding = 0) uniform sampler2D grid;
// layout(binding = 0, rgba8) uniform restrict image2D grid;

out vec4 out_Color;

in vec2 texCoords;

void main(){
	// out_Color = vec4(texCoords, 0, 1);
	out_Color = texture(grid, texCoords);
	// out_Color = imageLoad(grid, ivec2(texCoords * imageSize(grid)));
}