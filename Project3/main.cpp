#include "header.h"
#include "FrameBuffer.h"
#include "Mesh.h"
#include "Shader.h"

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
glm::vec3 camerapos(0.0f, 0.0f, 0.0f), camerafront(0.0f, 0.0f, -1.0f), cameraup(0.0f, 1.0f, 0.0f),cameradirection(1.0f,0.0f,1.0f);
float deltatime = 0.0f, lastframe = 0.0f;
float yaw = -90.0f, pitch = 0.0f;
bool firstmouse = true;
void convertpt(GLFWwindow* window, Point& pos);
void drawRect(int framebuffer1, Point& pos, Point& pos2);



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


void keypress(GLFWwindow* window) {
    float cameraspeed = 2.0f * deltatime;
    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) glfwSetWindowShouldClose(window, true);
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
        camerapos += cameraspeed * cameradirection;
    }
    if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
        camerapos -= cameraspeed * cameradirection;
    }
    if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) camerapos -= glm::normalize(glm::cross(camerafront, cameraup)) * cameraspeed;
    if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) camerapos += glm::normalize(glm::cross(camerafront, cameraup)) * cameraspeed;
    if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) camerapos += cameraspeed * glm::vec3(0.0f, 1.0f, 0.0f);
    if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) camerapos += cameraspeed * glm::vec3(0.0f, -1.0f, 0.0f);
}

int main(void)
{

    /* Initialize the library */
    if (!glfwInit())
        return -1;

    /* Create a windowed mode window and its OpenGL context */
    window = glfwCreateWindow(640, 480, "this is a title", NULL, NULL);
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
    /*glfwSetKeyCallback(window, [](GLFWwindow* window, int key, int scancode, int action, int mods) {
        
        });*/
    glfwSetWindowSizeCallback(window, [](GLFWwindow* window, int width, int height) {

        glfwSwapBuffers(window);
        glViewport(0, 0, width, height);
        std::cout << width << ' ' << height << std::endl;
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
        glm::vec3 direction,direction2;
        direction.x = cos(glm::radians(yaw)) * cos(glm::radians(pitch));
        direction.y = sin(glm::radians(pitch));
        direction.z = sin(glm::radians(yaw)) * cos(glm::radians(pitch));
        direction = glm::normalize(direction);
        direction2.x = cos(glm::radians(yaw)) * cos(glm::radians(0.0f));
        direction2.y = sin(glm::radians(0.0f));
        direction2.z = sin(glm::radians(yaw)) * cos(glm::radians(0.0f));
        direction2 = glm::normalize(direction2);
        camerafront = direction;
        cameradirection = direction2;
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
    unsigned int vbo,vao, ebo;
    Shader shader = Shader("vertexshader.glsl", "fragmentshader.glsl");
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

    shader.use();
    glm::mat4 projection(1.0f);
    
    projection = glm::perspective(glm::radians(120.0f), (float)680 / (float)480, 0.1f, 100.0f);
    glUniformMatrix4fv(glGetUniformLocation(shader.getId(), "projection"), 1, GL_FALSE, glm::value_ptr(projection));
    
    /* Loop until the user closes the window */
    glm::vec3 idk[] = { glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.2f,0.0f,0.0f) };
    std::vector<glm::vec3> vectorvector;
    for (float i = -10.0f; i <= 10.0f; i += 0.2f) {
        for (float j = -10.0f; j <= 10.0f; j += 0.2f) {
            vectorvector.push_back(glm::vec3(j, -0.2f, i));
        }
    }

    while (!glfwWindowShouldClose(window))
    {
        float currentFrame = glfwGetTime();
        deltatime = currentFrame - lastframe;
        lastframe = currentFrame;
        keypress(window);

        /* Render here */
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0, 0.0, 0.0, 1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, framebuffer.getFrameTexture());
        shader.use();

        glm::mat4 model = glm::mat4(1.0f), view(1.0f);
        
        //view = glm::translate(view, glm::vec3(0.0f, 0.0f, -3.0f));
        view = glm::lookAt(camerapos,camerapos+camerafront,cameraup);
        

        
        glUniformMatrix4fv(glGetUniformLocation(shader.getId(), "view"), 1, GL_FALSE, glm::value_ptr(view));
        glBindVertexArray(vao);
        //for (int i = 0; i < 2; i++) {
        //    model = glm::mat4(1.0f);
        //    model = glm::translate(model, idk[i]);
        //    //model = glm::rotate(model, 0.0f, glm::vec3(0.5, 1.0, 0.0));
        //    glUniformMatrix4fv(glGetUniformLocation(shaderprogram, "model"), 1, GL_FALSE, glm::value_ptr(model));
        //    glDrawArrays(GL_TRIANGLES, 0, 36);
        //}
        for (int i = 0; i < vectorvector.size(); i++) {
            model = glm::mat4(1.0f);
            model = glm::translate(model, vectorvector[i]);
            //model = glm::rotate(model, 0.0f, glm::vec3(0.5, 1.0, 0.0));
            glUniformMatrix4fv(glGetUniformLocation(shader.getId() , "model"), 1, GL_FALSE, glm::value_ptr(model));
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
    glfwTerminate();
    return 0;
}