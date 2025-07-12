using Interop.UIAutomationClient;
using System.Drawing;
using System.Runtime.InteropServices;

namespace ohmygod
{
    
    public partial class Form1 : Form
    {

        [DllImport("user32.dll")]
        public static extern void mouse_event(uint dwFlags, uint dx, uint dy, int cButtons, int dwExtraInfo);

        [DllImport("user32.dll", SetLastError = true)]
        public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        [DllImport("user32.dll")]
        public static extern bool GetWindowRect(IntPtr hWnd, out RECT lpRect);

        [DllImport("user32.dll")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern IntPtr SetFocus(IntPtr hWnd);

        [DllImport("user32.dll")]
        public static extern IntPtr SetCursorPos(int X, int Y);

        [DllImport("user32.dll")]
        public static extern bool ClientToScreen(IntPtr hWnd, ref Point lpPoint);

        [DllImport("user32.dll")]
        internal static extern uint SendInput(uint nInputs, [MarshalAs(UnmanagedType.LPArray), In] INPUT[] pInputs, int cbSize);

        internal struct INPUT
        {
            public UInt32 Type;
            public MOUSEKEYBDHARDWAREINPUT Data;
        }
        [StructLayout(LayoutKind.Explicit)]
        internal struct MOUSEKEYBDHARDWAREINPUT
        {
            [FieldOffset(0)]
            public MOUSEINPUT Mouse;
        }

        internal struct MOUSEINPUT
        {
            public Int32 X;
            public Int32 Y;
            public UInt32 MouseData;
            public UInt32 Flags;
            public UInt32 Time;
            public IntPtr ExtraInfo;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct RECT
        {
            public int Left; public int Top; public int Right; public int Bottom;
        }

        

        public const uint MOUSEEVENTF_LEFTDOWN = 0x02;
        public const uint MOUSEEVENTF_LEFTUP = 0x04;

        IntPtr wnd = IntPtr.Zero;
        public Form1()
        {
            InitializeComponent();
        }


        private void Form1_Load(object sender, EventArgs e)
        {
            while(wnd == IntPtr.Zero)
            {
                wnd = FindWindow(null, "메리츠증권 iMeritz XII 로그인");
                Thread.Sleep(1000);
            }
            button1.Visible = true;
        }

        public static void mouseclick()
        {
            var inputMouseDown = new INPUT();
            inputMouseDown.Type = 0;
            inputMouseDown.Data.Mouse.Flags = 0x0002;
            var inputMouseUp = new INPUT();
            inputMouseUp.Type = 0;
            inputMouseUp.Data.Mouse.Flags = 0x0004;
            var inputs = new INPUT[] { inputMouseDown, inputMouseUp };
            SendInput((uint)inputs.Length, inputs, Marshal.SizeOf(typeof(INPUT)));
        }

        public void test()
        {
            var automation = new CUIAutomation();

            // Get the desktop root element
            IUIAutomationElement root = automation.GetRootElement();

            // 1. Find the window by its title (NameProperty)
            string windowTitle = "메리츠증권 iMeritz XII 로그인"; // <-- change to your window title
            IUIAutomationCondition windowCondition = automation.CreatePropertyCondition(
                UIA_PropertyIds.UIA_NamePropertyId,
                windowTitle
            );

            IUIAutomationElement window = automation.ElementFromHandle(wnd);

            if (window == null)
            {
                Console.WriteLine("Window not found.");
                return;
            }

            Console.WriteLine("Window found!");

            var emptyNameCondition = automation.CreatePropertyCondition(UIA_PropertyIds.UIA_NamePropertyId, "");
            var dialogTypeCondition = automation.CreatePropertyCondition(UIA_PropertyIds.UIA_ControlTypePropertyId, UIA_ControlTypeIds.UIA_WindowControlTypeId);
            var dialogCondition = automation.CreateAndCondition(emptyNameCondition, dialogTypeCondition);
            var dialog = window.FindFirst(TreeScope.TreeScope_Children, dialogCondition);

            if (dialog == null)
            {
                MessageBox.Show("Dialog not found!");
                return;
            }

            var buttonIdCondition = automation.CreatePropertyCondition(UIA_PropertyIds.UIA_AutomationIdPropertyId, "1003");
            var buttonTypeCondition = automation.CreatePropertyCondition(UIA_PropertyIds.UIA_ControlTypePropertyId, UIA_ControlTypeIds.UIA_ButtonControlTypeId);
            var buttonCondition = automation.CreateAndCondition(buttonIdCondition, buttonTypeCondition);
            var button = dialog.FindFirst(TreeScope.TreeScope_Descendants, buttonCondition);

            var invokePattern = (IUIAutomationInvokePattern)button.GetCurrentPattern(UIA_PatternIds.UIA_InvokePatternId);
            invokePattern.Invoke();
            Console.WriteLine("Button clicked!");
        }
        private void button1_Click(object sender, EventArgs e)
        {
            if(true) { test(); return; }
            RECT rect;
            if (GetWindowRect(wnd, out rect))
            {
                int windowWidth = rect.Right - rect.Left;
                int windowHeight = rect.Bottom - rect.Top;

                Console.WriteLine($"Window Width: {windowWidth}, Height: {windowHeight}");
                double clientXPercent = 0.67;
                double clientYPercent = 0.79;

                int clientX = rect.Left + (int)(clientXPercent * (windowWidth));
                int clientY = rect.Top + (int)(clientYPercent * (windowHeight));

                Point point = new Point { X = clientX, Y = clientY };
                //ClientToScreen(wnd, ref point);
                SetCursorPos(point.X, point.Y);
                Console.WriteLine($"Window Width: {point.X}, Height: {point.Y}");
                //SetForegroundWindow(wnd);
                //SetFocus(wnd);

                //mouseclick();
                mouse_event(MOUSEEVENTF_LEFTDOWN, (uint)point.X, (uint)point.Y, 0, 0);
                mouse_event(MOUSEEVENTF_LEFTUP, (uint)point.X, (uint)point.Y, 0, 0);
            }
        }
    }
}
