#include <GLFW/glfw3.h>
#include <gl/GL.h>
#include <iostream>
#include <vector>


using namespace std;
class Point {
    double x, y;
public:
    Point() : x(0), y(0) {};
    Point(double a, double b) : x(a), y(b) {};
    double getX() { return x; }
    double getY() { return y; }
};

Point pos;
Point pos2;

void drawRect(Point& pos, Point& pos2) {
    Point top1 = Point(pos.getX(), pos.getY());
    Point top2 = Point(pos2.getX(), pos.getY());
    Point bottom1 = Point(pos2.getX(), pos2.getY());
    Point bottom2 = Point(pos.getX(), pos2.getY());
    glBegin(GL_LINE_LOOP);
    glColor3b(0, 0, 0);
    glVertex2f(top1.getX(), top1.getY());
    glVertex2f(top2.getX(), top2.getY());
    glVertex2f(bottom1.getX(), bottom1.getY());
    glVertex2f(bottom2.getX(), bottom2.getY());
    glEnd();
}

void getpt(GLFWwindow* window, double& xpos, double& ypos) {
    glfwGetCursorPos(window, &xpos, &ypos);
    int width, height;
    glfwGetWindowSize(window, &width, &height);
    xpos = (xpos / width) * 2.0f - 1.0f;
    ypos = -((ypos / height) * 2.0f - 1.0f);
}

int main(void)
{
    GLFWwindow* window;

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
    glfwSetKeyCallback(window, [](GLFWwindow* window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true);
            std::cout << key << std::endl;
        }
        });
    glfwSetWindowSizeCallback(window, [](GLFWwindow* window, int width, int height) {

        glfwSwapBuffers(window);
        std::cout << width << ' ' << height << std::endl;
        });
    glfwSetMouseButtonCallback(window, [](GLFWwindow* window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1) {
            if (action) {
                double xpos, ypos;
                getpt(window, xpos, ypos);
                pos = Point(xpos, ypos);
            }
        }
        });
    glfwSetCursorPosCallback(window, [](GLFWwindow* window, double xpos, double ypos) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1) {
            double xpos, ypos;
            getpt(window, xpos, ypos);
            pos2 = Point(xpos, ypos);
        }
        });
    glfwSwapInterval(1);
    /* Loop until the user closes the window */
    while (!glfwWindowShouldClose(window))
    {
        int width, height;
        glfwGetFramebufferSize(window, &width, &height);
        glViewport(0, 0, width, height);
        /* Render here */
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(255, 255, 255, 0);

        drawRect(pos, pos2);

        /* Swap front and back buffers */
        glfwSwapBuffers(window);

        /* Poll for and process events */
        glfwPollEvents();
    }

    glfwTerminate();
    return 0;
}