using System;
using System.ComponentModel;
using System.Linq;

namespace Utility
{
    public class Program
    {
        public static bool BoolTest(bool? input)
        {
            return input ?? false;
        }

        public static bool IsListNotNullOrEmpty(List<string>? list)
        {
            return list?.Count > 0;
        }

        public static string? RemveNonAlphanumeric(string? input)
        {
            if (string.IsNullOrEmpty(input))
            {
                return null;
            }
            System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex(@"[^a-zA-Z\d\s]");
            return rgx.Replace(input, "");
        }

        public static bool IsNumeric(string input)
        {
            if (string.IsNullOrEmpty(input))
            {
                return false;
            }
            System.Text.RegularExpressions.Regex rgx = new System.Text.RegularExpressions.Regex(@"^\d+$");
            return rgx.IsMatch(new System.Text.RegularExpressions.Regex(@"\s").Replace(input, ""));
        }
    }

    public enum AssetType
    {
        [Description("11")]
        AFund = 1,
        [Description("22")]
        HFund = 2,
        [Description("33")]
        PFund = 3
    }

    public enum TestEnum
    {
        [Description("A&B")]
        AnB,
        [Description("C & D")]
        isCD,
        [Description("Include E")]
        INCLUDEE,
        [Description("R")]
        R
    }
}