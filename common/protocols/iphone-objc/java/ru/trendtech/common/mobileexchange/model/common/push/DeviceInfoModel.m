#import "DeviceInfoModel.h"

@implementation DeviceInfoModel

@synthesize token;
@synthesize deviceType;

- (void) dealloc {
  [token release];
  [super dealloc];
}

@end
