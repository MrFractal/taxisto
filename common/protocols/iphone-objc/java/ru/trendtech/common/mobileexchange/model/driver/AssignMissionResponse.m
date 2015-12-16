#import "AssignMissionResponse.h"

@implementation AssignMissionResponse

@synthesize assigned;

- (void) init {
  if (self = [super init]) {
    assigned = NO;
  }
  return self;
}

@end
