namespace Utility.Tests;

public class HelperTest
{
    [Fact]
    public void TestBoolTest()
    {
        Assert.False(Program.BoolTest(null));
        Assert.True(Program.BoolTest(true));
        Assert.False(Program.BoolTest(false));
    }

    [Fact]
    public void TestIsListNotNullOrEmpty()
    {
        Assert.False(Program.IsListNotNullOrEmpty(null));
        Assert.False(Program.IsListNotNullOrEmpty(new List<string> { }));
        Assert.True(Program.IsListNotNullOrEmpty(new List<string> { "aa" }));
    }

    [Fact]
    public void TestRemveNonAlphanumeric()
    {
        Assert.Null(Program.RemveNonAlphanumeric(null));
        Assert.Equal("Send  Reply 1 2", Program.RemveNonAlphanumeric("Send & Reply 1 2"));
        Assert.Equal("\r\n\t", Program.RemveNonAlphanumeric("-&./,;'\"!@#$%^&*()\r\n\t"));
        Assert.Equal("ABCDEDFGHIJKLMNOPQRSTUVWXYZ0123456789abcdedfghijklmnopqrstuvwxyz", Program.RemveNonAlphanumeric("ABCDEDFGHIJKLMNOPQRSTUVWXYZ0123456789abcdedfghijklmnopqrstuvwxyz"));
    }

    [Fact]
    public void TestIsNumeric()
    {
        Assert.False(Program.IsNumeric("Send & Reply 1 2"));
        Assert.True(Program.IsNumeric("132432"));
        Assert.True(Program.IsNumeric("43214123 "));
    }

    [Fact]
    public void TestAssetType()
    {
        Assert.Equal(AssetType.AFund, (AssetType)1);
        Assert.Equal(AssetType.HFund, (AssetType)2);
        Assert.Equal(AssetType.PFund, (AssetType)3);

        Assert.True(Enum.IsDefined(typeof(AssetType), 1));
    }

    [Fact]
    public void TestTestEnum()
    {
        Array array = Enum.GetNames(typeof(TestEnum));
        Assert.Equal("AnB", array.GetValue(0)?.ToString());
        Assert.Equal(TestEnum.AnB, Enum.Parse(typeof(TestEnum), "AnB"));
        Assert.Equal(TestEnum.isCD, Enum.Parse(typeof(TestEnum), "isCD"));
        Assert.Equal(TestEnum.INCLUDEE, Enum.Parse(typeof(TestEnum), "INCLUDEE"));
        Assert.Equal(TestEnum.R, Enum.Parse(typeof(TestEnum), "R"));
    }

    [Fact]
    public void TestTestEnumException()
    {
        Action testCode = () => { Enum.Parse(typeof(TestEnum), "AnB1 ", true); };
        var ex = Record.Exception(testCode);
        Assert.NotNull(ex);
        Assert.Equal("Requested value 'AnB1 ' was not found.", ex.Message);
        Assert.IsType<System.ArgumentException>(ex);
    }

    [Fact]
    public void TestClass()
    {
        Guid id = Guid.NewGuid();
        string name = "testName";
        Guid createdBy = Guid.NewGuid();
        {
            TestClass testClazzWithNothing = new TestClass();
            Assert.Equal(testClazzWithNothing.Id, Guid.Empty);
            Assert.Null(testClazzWithNothing.Name);
            Assert.Null(testClazzWithNothing.CreatedBy);
        }

        {
            TestClass testClazzWithId = new TestClass()
            {
                Id = id,
                Name = name,
            };
            Assert.Equal(testClazzWithId.Id, id);
            Assert.Equal(testClazzWithId.Name, name);
            Assert.Null(testClazzWithId.CreatedBy);
        }

        {
            TestClass testClazzWithCreatedBy = new TestClass()
            {
                Id = id,
                Name = name,
                CreatedBy = createdBy,
            };
            Assert.Equal(testClazzWithCreatedBy.Id, id);
            Assert.Equal(testClazzWithCreatedBy.Name, name);
            Assert.Equal(testClazzWithCreatedBy.CreatedBy, createdBy);
        }

        {
            TestClass? testClazz = null;
            Assert.Null(testClazz?.Id);
            Assert.Null(testClazz?.Name);
            Assert.Null(testClazz?.CreatedBy);
        }
    }
}