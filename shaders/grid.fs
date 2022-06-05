#version 460 core

layout(binding = 0) uniform sampler2D grid;
// layout(binding = 0, rgba8) uniform restrict image2D grid;

out vec4 out_Color;

in vec2 texCoords;

uniform ivec2 mouseCoords;
uniform float radius;


void main(){
	ivec4 c = ivec4(texture(grid, texCoords) * 255.0f);
	int type = c.a;
	ivec2 velocity = c.xy;

	if(type == 0){
		out_Color = vec4(0, 0, 0, 0);
	}
	if(type == 1){
		out_Color = vec4(0, 1, 0, 1);
	}

	// out_Color = imageLoad(grid, ivec2(texCoords * imageSize(grid)));

	float d = length(ivec2(texCoords * textureSize(grid, 0)) - mouseCoords);
	if(d < radius && d > 0.9 * radius){
        out_Color = vec4(1, 0, 1, 1);
    }
}