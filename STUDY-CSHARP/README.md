# STUDY-CSHARP
This is a experiment project created and tested on `Windows 11` OS and `dotnet 4.8.09032`.
```
PS D:\Test\STUDY-CSHARP> Get-ChildItem 'HKLM:\SOFTWARE\Microsoft\NET Framework Setup\NDP' -Recurse | Get-ItemProperty -Name version -EA 0 | Where { $_.PSChildName -Match '^(?!S)\p{L}'} | Select PSChildName, version

PSChildName Version
----------- -------
Client      4.8.09032
Full        4.8.09032
Client      4.0.0.0


PS D:\Test\STUDY-CSHARP>
```
More information on [在Visual-Studio-Code中创建一个带xUnit的简单C#项目](https://hivsuper.github.io/posts/%E5%9C%A8Visual-Studio-Code%E4%B8%AD%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AA%E5%B8%A6xUnit%E7%9A%84%E7%AE%80%E5%8D%95C-%E9%A1%B9%E7%9B%AE/)