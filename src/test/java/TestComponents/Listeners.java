package TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import Resources.ExtentReporter;

import java.io.IOException;

public class Listeners extends BaseTest implements ITestListener {

    ExtentReports extent = ExtentReporter.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>(); // Thread-safe ExtentTest

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test); // Set the test object in the current thread
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());

        WebDriver driver = null;
        try {
            // Fetch the thread-safe WebDriver instance
            driver = getDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String filePath = null;
        try {
            if (driver != null) {
                filePath = getScreenshot(result.getMethod().getMethodName(), driver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (filePath != null) {
            try {
                extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
//        extentTest.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Optional: Implement if needed
    }

    @Override
    public void onStart(ITestContext context) {
        // Called before the suite starts
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush(); // Flush all logs to the Extent Report
    }
}
