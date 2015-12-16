#import "PushMessage.h"

@implementation PushMessage

@synthesize t;
@synthesize o;

- (void) init {
  if (self = [super init]) {
    t = -1;
    o = @"";
  }
  return self;
}

- (void) dealloc {
  [o release];
  [super dealloc];
}

@end
