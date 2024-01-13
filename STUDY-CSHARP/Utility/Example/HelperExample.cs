namespace Utility.Example
{
    public class HelperExample
    {
        public static void Main(string[] args)
        {
            Console.WriteLine(Program.IsNumeric("Send & Reply 1 2"));
            Console.WriteLine(Program.IsNumeric("132432"));
            Console.WriteLine(Program.IsNumeric("43214123 "));
        }
    }
}