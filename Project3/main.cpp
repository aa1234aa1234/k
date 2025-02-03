#define GLAD_GL_IMPLEMENTATION
#include <glad/gl.h>
#define GLFW_INCLUDE_NONE
#include <GLFW/glfw3.h>


#include <iostream>
#include <vector>
#include "FrameBuffer.h"
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>


using namespace std;


class Point {
    double x, y;
public:
    Point() : x(0), y(0) {};
    Point(double a, double b) : x(a), y(b) {};
    double getX() { return x; }
    double getY() { return y; }
    void setX(double a) { x = a; }
    void setY(double a) { y = a; }
};

Point pos;
Point pos2;
Point lastpos;
GLFWwindow* window;
FrameBuffer framebuffer = FrameBuffer();
glm::vec3 camerapos(0.0f, 0.0f, 3.0f), camerafront(0.0f, 0.0f, -1.0f), cameraup(0.0f, 1.0f, 0.0f);
float deltatime = 0.0f, lastframe = 0.0f;
float yaw = -90.0f, pitch = 0.0f;
bool firstmouse = true;
void convertpt(GLFWwindow* window, Point& pos);
void drawRect(int framebuffer1, Point& pos, Point& pos2);
const char* shader = "#version 330 core\n"
"layout(location = 0) in vec3 aPos;\n"
"layout(location = 1) in vec2 aTexCoord;\n"
"out vec4 vertexColor;\n"
"out vec2 TexCoord;\n"
"uniform mat4 model;\n"
"uniform mat4 view;\n"
"uniform mat4 projection;\n"
"void main()\n"
"{\n"
"    gl_Position = projection * view * model * vec4(aPos.x, aPos.y, aPos.z, 1.0);\n"
"    TexCoord = aTexCoord;\n"
"    vertexColor = vec4(0.5,0.0,0.0,1.0);\n"
"}\0";
const char* shader2 = "#version 330 core\n"
"out vec4 FragColor;\n"
"in vec4 vertexColor;\n"
"in vec2 TexCoord;\n"
"uniform sampler2D ourTexture;\n"
"void main()\n"
"{\n"
"    FragColor = texture(ourTexture,TexCoord);\n"
"}\0";

std::ostream& operator<<(std::ostream& stream, glm::vec4& vector) {
    stream << vector.x << ' ' << vector.y << ' ' << vector.z << ' ' << vector.w << std::endl;
    return stream;
}

std::ostream& operator<<(std::ostream& stream, glm::vec3& vector) {
    stream << vector.x << ' ' << vector.y << ' ' << vector.z << std::endl;
    return stream;
}

void init() {
    int width, height;
    glfwGetFramebufferSize(window, &width, &height);
    framebuffer.init(width, height);
    framebuffer.bind();
    glClearColor(0.5, 1.0, 1.0, 1.0);
    glClear(GL_COLOR_BUFFER_BIT);
    framebuffer.unbind();
    lastpos = Point(width / 2, height / 2);
}

void drawRect(int framebuffer1, Point& pos, Point& pos2) {
    Point top1 = Point(pos.getX(), pos.getY());
    Point top2 = Point(pos2.getX(), pos.getY());
    Point bottom1 = Point(pos2.getX(), pos2.getY());
    Point bottom2 = Point(pos.getX(), pos2.getY());
    glBindFramebuffer(GL_FRAMEBUFFER, framebuffer1);
    glBindTexture(GL_TEXTURE_2D,0);
    glBegin(GL_LINE_LOOP);
    glColor3b(1.0, 1.0, 1.0);
    glVertex2f(top1.getX(), top1.getY());
    glVertex2f(top2.getX(), top2.getY());
    glVertex2f(bottom1.getX(), bottom1.getY());
    glVertex2f(bottom2.getX(), bottom2.getY());
    glEnd();
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

void getpt(GLFWwindow* window, double& xpos, double& ypos) {
    glfwGetCursorPos(window, &xpos, &ypos);
    int width, height;
    glfwGetWindowSize(window, &width, &height);
    xpos = (xpos / width) * 2.0f - 1.0f;
    ypos = -((ypos / height) * 2.0f - 1.0f);
}

void convertpt(GLFWwindow* window, Point& pos) {
    double xpos, ypos;
    glfwGetCursorPos(window, &xpos, &ypos);
    int width, height;
    glfwGetWindowSize(window, &width, &height);
    pos.setX((xpos / width) * 2.0f - 1.0f);
    pos.setY(-((ypos / height) * 2.0f - 1.0f));
}

int main(void)
{

    /* Initialize the library */
    if (!glfwInit())
        return -1;

    /* Create a windowed mode window and its OpenGL context */
    window = glfwCreateWindow(640, 480, "Hello World", NULL, NULL);
    if (!window)
    {
        glfwTerminate();
        return -1;
    }
    /* Make the window's context current */
    glfwMakeContextCurrent(window);
    gladLoadGL(glfwGetProcAddress);
    int w, h;
    glfwGetFramebufferSize(window, &w, &h);
    glViewport(0, 0, w, h);
    init();
    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    glfwSetKeyCallback(window, [](GLFWwindow* window, int key, int scancode, int action, int mods) {
        float cameraspeed = 5.0f * deltatime;
        if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true);
        if (key == GLFW_KEY_W) {
            camerapos += cameraspeed * camerafront;
        }
        if (key == GLFW_KEY_S) {
            camerapos -= cameraspeed * camerafront;
        }
        if (key == GLFW_KEY_A) camerapos -= glm::normalize(glm::cross(camerafront, cameraup)) * cameraspeed;
        if (key == GLFW_KEY_D) camerapos += glm::normalize(glm::cross(camerafront, cameraup)) * cameraspeed;
        });
    glfwSetWindowSizeCallback(window, [](GLFWwindow* window, int width, int height) {

        glfwSwapBuffers(window);
        glViewport(0, 0, width, height);
        std::cout << width << ' ' << height << std::endl;
        });
    glfwSetMouseButtonCallback(window, [](GLFWwindow* window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1) {
            if (action) {
                double xpos, ypos;
                getpt(window, xpos, ypos);
                pos = Point(xpos, ypos);
            }
            else {
                //drawRect(framebuffer.getFrameBuffer(), pos, pos2);
            }
        }
        });
    
    glfwSetCursorPosCallback(window, [](GLFWwindow* window, double xpos, double ypos) {
        /*if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1) {
            double xpos, ypos;
            getpt(window, xpos, ypos);
            pos2 = Point(xpos, ypos);
        }*/
        if (firstmouse) {
            lastpos.setX(xpos); lastpos.setY(ypos);
            firstmouse = false;
        }
        Point offset(xpos-lastpos.getX(), lastpos.getY() - ypos);
        lastpos.setX(xpos); lastpos.setY(ypos);
        float sensitivity = 0.1f;
        offset.setX(offset.getX() * sensitivity);
        offset.setY(offset.getY() * sensitivity);
        yaw += offset.getX();
        pitch += offset.getY();
        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;
        glm::vec3 direction;
        direction.x = cos(glm::radians(yaw)) * cos(glm::radians(pitch));
        direction.y = sin(glm::radians(pitch));
        direction.z = sin(glm::radians(yaw)) * cos(glm::radians(pitch));
        direction = glm::normalize(direction);
        camerafront = direction;
        std::cout << camerafront << std::endl;
        });
    glfwSwapInterval(1);
    /*float vertices[] = {
        1.0f,1.0f,0.0f,1.0f,1.0f,
        1.0f,-1.0f,0.0f,1.0f,0.0f,
        -1.0f,-1.0f,0.0f,0.0f,0.0f,
        -1.0f,1.0f,0.0f,0.0f,1.0f
    };*/
    /*float vertices[] = {
        0.5f,0.5f,0.0f,1.0f,1.0f,
        0.5f,-0.5f,0.0f,1.0f,0.0f,
        -0.5f,-0.5f,0.0f,0.0f,0.0f,
        -0.5f,0.5f,0.0f,0.0f,1.0f
    };*/
    float vertices[] = {
    -0.1f, -0.1f, -0.1f,  0.0f, 0.0f,
     0.1f, -0.1f, -0.1f,  1.0f, 0.0f,
     0.1f,  0.1f, -0.1f,  1.0f, 1.0f,
     0.1f,  0.1f, -0.1f,  1.0f, 1.0f,
    -0.1f,  0.1f, -0.1f,  0.0f, 1.0f,
    -0.1f, -0.1f, -0.1f,  0.0f, 0.0f,

    -0.1f, -0.1f,  0.1f,  0.0f, 0.0f,
     0.1f, -0.1f,  0.1f,  1.0f, 0.0f,
     0.1f,  0.1f,  0.1f,  1.0f, 1.0f,
     0.1f,  0.1f,  0.1f,  1.0f, 1.0f,
    -0.1f,  0.1f,  0.1f,  0.0f, 1.0f,
    -0.1f, -0.1f,  0.1f,  0.0f, 0.0f,

    -0.1f,  0.1f,  0.1f,  1.0f, 0.0f,
    -0.1f,  0.1f, -0.1f,  1.0f, 1.0f,
    -0.1f, -0.1f, -0.1f,  0.0f, 1.0f,
    -0.1f, -0.1f, -0.1f,  0.0f, 1.0f,
    -0.1f, -0.1f,  0.1f,  0.0f, 0.0f,
    -0.1f,  0.1f,  0.1f,  1.0f, 0.0f,

     0.1f,  0.1f,  0.1f,  1.0f, 0.0f,
     0.1f,  0.1f, -0.1f,  1.0f, 1.0f,
     0.1f, -0.1f, -0.1f,  0.0f, 1.0f,
     0.1f, -0.1f, -0.1f,  0.0f, 1.0f,
     0.1f, -0.1f,  0.1f,  0.0f, 0.0f,
     0.1f,  0.1f,  0.1f,  1.0f, 0.0f,

    -0.1f, -0.1f, -0.1f,  0.0f, 1.0f,
     0.1f, -0.1f, -0.1f,  1.0f, 1.0f,
     0.1f, -0.1f,  0.1f,  1.0f, 0.0f,
     0.1f, -0.1f,  0.1f,  1.0f, 0.0f,
    -0.1f, -0.1f,  0.1f,  0.0f, 0.0f,
    -0.1f, -0.1f, -0.1f,  0.0f, 1.0f,

    -0.1f,  0.1f, -0.1f,  0.0f, 1.0f,
     0.1f,  0.1f, -0.1f,  1.0f, 1.0f,
     0.1f,  0.1f,  0.1f,  1.0f, 0.0f,
     0.1f,  0.1f,  0.1f,  1.0f, 0.0f,
    -0.1f,  0.1f,  0.1f,  0.0f, 0.0f,
    -0.1f,  0.1f, -0.1f,  0.0f, 1.0f
    };
    unsigned int indices[] = {
        0,1,3,
        1,2,3
    };
    unsigned int vertexshader, fragmentshader, vbo, shaderprogram, vao, ebo;
    vertexshader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexshader, 1, &shader, NULL);
    glCompileShader(vertexshader);
    int success;
    char infoLog[512];
    glGetShaderiv(vertexshader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(vertexshader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }
    fragmentshader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentshader, 1, &shader2, NULL);
    glCompileShader(fragmentshader);

    glGetShaderiv(fragmentshader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(fragmentshader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }
    shaderprogram = glCreateProgram();
    glAttachShader(shaderprogram, vertexshader);
    glAttachShader(shaderprogram, fragmentshader);
    glLinkProgram(shaderprogram);
    glDeleteShader(vertexshader);
    glDeleteShader(fragmentshader);
    glGenBuffers(1, &vbo);
    glGenVertexArrays(1, &vao);
    glGenBuffers(1, &ebo);
    glBindVertexArray(vao);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

    glUseProgram(shaderprogram);
    glm::mat4 projection(1.0f);
    
    projection = glm::perspective(glm::radians(45.0f), (float)680 / (float)480, 0.1f, 100.0f);
    glUniformMatrix4fv(glGetUniformLocation(shaderprogram, "projection"), 1, GL_FALSE, glm::value_ptr(projection));

    
    
    /* Loop until the user closes the window */
    glm::vec3 idk[] = { glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.2f,0.0f,0.0f) };
    while (!glfwWindowShouldClose(window))
    {
        float currentFrame = glfwGetTime();
        deltatime = currentFrame - lastframe;
        lastframe = currentFrame;
        /* Render here */
        glClearColor(1.0, 1.0, 1.0, 1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, framebuffer.getFrameTexture());
        glUseProgram(shaderprogram);
        glm::mat4 model = glm::mat4(1.0f), view(1.0f);
        
        //view = glm::translate(view, glm::vec3(0.0f, 0.0f, -3.0f));
        view = glm::lookAt(camerapos,camerapos+camerafront,cameraup);
        

        
        glUniformMatrix4fv(glGetUniformLocation(shaderprogram, "view"), 1, GL_FALSE, glm::value_ptr(view));
        glBindVertexArray(vao);
        for (int i = 0; i < 2; i++) {
            model = glm::mat4(1.0f);
            model = glm::translate(model, idk[i]);
            //model = glm::rotate(model, 0.0f, glm::vec3(0.5, 1.0, 0.0));
            glUniformMatrix4fv(glGetUniformLocation(shaderprogram, "model"), 1, GL_FALSE, glm::value_ptr(model));
            glDrawArrays(GL_TRIANGLES, 0, 36);
        }
        
        //glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);


        /* Swap front and back buffers */
        glfwSwapBuffers(window);

        /* Poll for and process events */
        glfwPollEvents();
    }
    glDeleteVertexArrays(1, &vao);
    glDeleteBuffers(1, &vbo);
    glDeleteBuffers(1, &ebo);
    glDeleteProgram(shaderprogram);
    glfwTerminate();
    return 0;
}