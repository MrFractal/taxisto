#import "LogoutResponse.h"

@implementation LogoutResponse

@synthesize completed;
@synthesize reason;
@synthesize reasonMessage;

- (void) dealloc {
  [reasonMessage release];
  [super dealloc];
}

@end
