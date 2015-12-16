#import "DriverInfo.h"
#import "MissionInfo.h"

/**
 * Created by max on 06.02.14.
 */

@interface AssignedDriverResponse : NSObject {
  DriverInfo * driverInfo;
  int expectedTime;
}

@property(nonatomic) int expectedTime;
@property(nonatomic, retain) DriverInfo * driverInfo;
- (void) setExpectedTime:(int)expectedTime;
- (void) setDriverInfo:(DriverInfo *)driverInfo;
@end
