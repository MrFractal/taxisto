#import "UpdateDeviceInfoRequest.h"

@implementation UpdateDeviceInfoRequest

@synthesize requesterId;
@synthesize deviceInfoModel;

- (void) dealloc {
  [deviceInfoModel release];
  [super dealloc];
}

@end
