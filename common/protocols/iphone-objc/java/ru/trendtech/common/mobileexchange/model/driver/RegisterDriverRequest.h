#import "DriverInfo.h"

/**
 * Created by max on 09.02.14.
 */

@interface RegisterDriverRequest : NSObject {
  DriverInfo * driverInfo;
}

@property(nonatomic, retain) DriverInfo * driverInfo;
- (void) setDriverInfo:(DriverInfo *)driverInfo;
@end
