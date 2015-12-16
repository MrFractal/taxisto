#import "DriverInfo.h"

/**
 * Created by max on 07.02.14.
 */

@interface LoginDriverResponse : NSObject {
  DriverInfo * driverInfo;
}

@property(nonatomic, retain) DriverInfo * driverInfo;
- (void) setDriverInfo:(DriverInfo *)driverInfo;
@end
