#ifndef MOUSEMOVEPUSH_H
#define MOUSEMOVEPUSH_H

#include <X11/Xatom.h>
#include <X11/Xlib.h>
#include <stdio.h>

class MouseMovePush{
private:
    Display *dpy;
    Screen *scr;
    XEvent event;
public:

    MouseMovePush(){
       dpy= XOpenDisplay(0);
        scr=DefaultScreenOfDisplay(dpy);
    }

    void moveMouse(int x,int y){
        Window root_window;
        root_window = XRootWindow(dpy, 0);
        XSelectInput(dpy, root_window, KeyReleaseMask);
        XWarpPointer(dpy, None, root_window, 0, 0, 0, 0, x, y);
        XFlush(dpy);
    }

    int getHeight(){
        return scr->height;
    }

    int getWidth(){
        return scr->width;
    }

    void pushButton(){
        event.type = ButtonPress;
        event.xbutton.button=1;
        event.xbutton.same_screen = True;
        XQueryPointer(dpy, RootWindow(dpy, DefaultScreen(dpy)), &event.xbutton.root,
                      &event.xbutton.window, &event.xbutton.x_root, &event.xbutton.y_root,
                      &event.xbutton.x, &event.xbutton.y, &event.xbutton.state);
        event.xbutton.subwindow = event.xbutton.window;
         if(XSendEvent(dpy, PointerWindow, True, 0xfff, &event) == 0) printf("error");
         XFlush(dpy);
    }

    void releaseMouse(){
        event.type = ButtonRelease;
        event.xbutton.state = 0x100;
        if(XSendEvent(dpy, PointerWindow, True, 0xfff, &event) == 0) printf("error");
        XFlush(dpy);
    }
};

#endif // MOUSEMOVEPUSH_H

